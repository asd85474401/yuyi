package com.kcidea.erms.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.csvreader.CsvReader;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kcidea.erms.common.constant.Constant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.mozilla.intl.chardet.nsDetector;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
@Slf4j
public class ExcelUtil {


    public static void saveExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String filePath, String fileName) {

        String fileSrc = filePath + fileName;

        // 得到保存任务文件的文件夹
        File fileDir = new File(filePath);
        // 如果文件夹不存在就创建
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(fileSrc, pojoClass).build();
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            int listSize = list.size();
            int toIndex = 100;
            for (int i = 0; i < list.size(); i += 100) {
                if (i + 100 > listSize) {
                    //作用为toIndex最后没有100条数据则剩余几条newList中就装几条
                    toIndex = listSize - i;
                }
                List newList = list.subList(i, i + toIndex);
                // 这里分批写入
                excelWriter.write(newList, writeSheet);
            }

            if (0 == listSize) {
                excelWriter.write(Lists.newArrayList(), writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 模型解析监听器 -- 每解析一行会回调invoke()方法，整个excel解析结束会执行doAfterAllAnalysed()方法
     */
    private static class ModelExcelListener<E> extends AnalysisEventListener<E> {
        private List<E> dataList = new ArrayList<E>();

        /**
         * 批处理阈值
         */
        private static final int BATCH_COUNT = 1000;
        List<E> list = new ArrayList<>(BATCH_COUNT);
        Map<Integer, String> headMap = Maps.newHashMap();
        List<Map<Integer, String>> excelHeadMapList = Lists.newArrayList();

        @Override
        public void invoke(E object, AnalysisContext context) {
            list.add(object);
            if (list.size() >= BATCH_COUNT) {
                saveData();
                list.clear();
            }
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            super.invokeHeadMap(headMap, context);
            Set<Integer> keySet = headMap.keySet();
            for (Integer key : keySet) {
                String head = headMap.get(key);
                head = head.replace("\n", " ");
                head = head.replace("\ufeff", "");
                this.headMap.put(key, head);
            }
            excelHeadMapList.add(this.headMap);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            saveData();
            log.info("所有数据解析完成！");
        }

        private void saveData() {
            dataList.addAll(list);
        }

        public List<E> getDataList() {
            return dataList;
        }

        public List<Map<Integer, String>> getExcelHeadMapList() {
            return excelHeadMapList;
        }

        public Map<Integer, String> getExcelHeadMap() {
            return headMap;
        }

        @SuppressWarnings("unused")
        public void setDataList(List<E> dataList) {
            this.dataList = dataList;
        }
    }

    /**
     * 使用 模型 读取Excel的表头集合和数据
     *
     * @param filePath    文件路径
     * @param headLineNum 表头行数
     * @param clazz       模型类型
     * @return <E>
     * @author yeweiwei
     * @date 2021/7/26 16:37
     */
    public static <E> ExcelInfo<E> readExcelHeadMapListAndData(String filePath, int headLineNum, Class<?> clazz) {
        ModelExcelListener listener = getListenerWithModel(filePath, headLineNum, clazz);
        ExcelInfo<E> excelInfo = new ExcelInfo<E>();
        excelInfo.setData(listener.getDataList());
        excelInfo.setHeadMapList(listener.getExcelHeadMapList());
        excelInfo.setHeadMap(listener.getExcelHeadMap());
        return excelInfo;
    }

    /**
     * 使用 模型 读取Excel的表头集合和数据
     *
     * @param inputStream 文件流
     * @param headLineNum 表头行数
     * @param clazz       模型类型
     * @return <E>
     * @author yeweiwei
     * @date 2021/7/26 16:37
     */
    public static <E> ExcelInfo<E> readExcelHeadMapListAndData(InputStream inputStream, int headLineNum, Class<?> clazz) {
        ModelExcelListener listener = getListenerWithModel(inputStream, headLineNum, clazz);
        ExcelInfo<E> excelInfo = new ExcelInfo<E>();
        excelInfo.setData(listener.getDataList());
        excelInfo.setHeadMapList(listener.getExcelHeadMapList());
        excelInfo.setHeadMap(listener.getExcelHeadMap());
        return excelInfo;
    }

    /**
     * 获取listener
     *
     * @param inputStream 文件流
     * @param headLineNum 表头行数
     * @param clazz       模型类型
     * @return listener
     * @author yeweiwei
     * @date 2021/7/26 16:43
     */
    private static ModelExcelListener getListenerWithModel(InputStream inputStream, int headLineNum, Class<?> clazz) {
        ModelExcelListener listener = new ModelExcelListener();
        try {
            EasyExcel.read(inputStream, clazz, listener).sheet().headRowNumber(headLineNum).doRead();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return listener;
    }

    /**
     * 获取listener
     *
     * @param filePath    文件路径
     * @param headLineNum 表头行数
     * @param clazz       模型类型
     * @return listener
     * @author yeweiwei
     * @date 2021/7/26 16:43
     */
    private static ModelExcelListener getListenerWithModel(String filePath, int headLineNum, Class<?> clazz) {
        InputStream in = null;
        ModelExcelListener listener = new ModelExcelListener();
        try {
            in = new FileInputStream(new File(filePath));
            EasyExcel.read(in, clazz, listener).sheet().headRowNumber(headLineNum).doRead();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return listener;
    }

    /**
     * 使用 模型 来读取Excel
     *
     * @param in
     * @param headLineNum
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> List<E> readExcel(InputStream in, int headLineNum, Class<?> clazz) {
        ModelExcelListener listener = new ModelExcelListener();

        EasyExcel.read(in, clazz, listener).sheet().headRowNumber(headLineNum).doRead();
        return listener.getDataList();
    }

    /**
     * easyExcel通过 文件路径 读取数据
     *
     * @param filePath
     * @param headLineNum
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> List<E> readExcelWithFilePath(String filePath, int headLineNum, Class clazz) {
        InputStream in = null;
        List dataList = null;
        try {
            in = new FileInputStream(new File(filePath));
            dataList = readExcel(in, headLineNum, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return dataList;
    }

    /**
     * easyExcel通过 文件类 读取数据
     *
     * @param excel
     * @param headLineNum
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> List<E> readExcelWithMultipartFile(MultipartFile excel, int headLineNum, Class<?> clazz) {
        List dataList = null;
        try {
            dataList = readExcel(excel.getInputStream(), headLineNum, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    @Data
    public static class ExcelInfo<E> {
        /**
         * 表头集合
         */
        private List<Map<Integer, String>> headMapList;

        /**
         * 表头
         */
        private Map<Integer, String> headMap;

        /**
         * 数据
         */
        private List<E> data;
    }

    /**
     * 将csv文件读成对象集合
     *
     * @param inputStream 流
     * @param headLineNum 标题行
     * @param clazz       对象类型
     * @return 读取的数据
     * @author yeweiwei
     * @date 2021/9/3 17:48
     */
    public static <T> ExcelInfo<T> readCsv(InputStream inputStream, Integer headLineNum, Class<T> clazz) {
        ExcelUtil.ExcelInfo<T> excelInfo = new ExcelUtil.ExcelInfo<>();
        try {
            //inputStream只能读取一次，需要处理一下
            byte[] dataBytes = getBytes(inputStream);
            //第一个用户读取编码方式
            InputStream in1 = new ByteArrayInputStream(dataBytes);
            //第二个用于读取表格内容
            InputStream in2 = new ByteArrayInputStream(dataBytes);

            //获取编码方式
            String code = new EncodeUtil().guessFileEncodingByInputStream(in1, new nsDetector());
            CsvReader reader = new CsvReader(in2, ',', Charset.forName(code));
            List<Map<Integer, String>> headMapList = Lists.newArrayList();
            Map<Integer, String> headMap = Maps.newHashMap();

            List<T> list = Lists.newArrayList();

            int rowNum = 0;
            Map<String, Integer> nameIndexMap = Maps.newHashMap();
            while (reader.readRecord()) {
                String[] row = reader.getValues();
                //获取表格正式开始前的数据行，如表头、标题行等
                if (rowNum < headLineNum) {
                    headMap.clear();
                    Map<Integer, String> rowMap = Maps.newHashMap();
                    for (int j = 0; j < row.length; j++) {
                        if (!Strings.isNullOrEmpty(row[j])) {
                            String head = row[j];
                            head = head.replace("\ufeff", "");
                            head = head.replace("\n", " ");
                            rowMap.put(j, head);
                        }
                    }
                    headMapList.add(rowMap);
                    headMap.putAll(rowMap);
                    rowNum++;
                    continue;
                }
                //将标题行和位置一一映射
                if (CollectionUtils.isEmpty(nameIndexMap) && rowNum == headLineNum) {
                    Set<Integer> indexs = headMap.keySet();
                    for (Integer index : indexs) {
                        String columnName = headMap.get(index);
                        nameIndexMap.put(columnName, index);
                    }
                }

                //创建对象，获取对象属性集合
                T obj = clazz.newInstance();
                Field[] fs = obj.getClass().getDeclaredFields();
                //遍历属性集合
                for (Field field : fs) {
                    field.setAccessible(true);
                    Class<?> filedType = field.getType();
                    if (field.isAnnotationPresent(ExcelProperty.class)) {
                        //获取属性上的注解，注解内容为列名
                        ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                        String[] columnName = annotation.value();
                        //如果列名不为空，根据nameIndexMap找到这个列名对应的位置，从读取的这行数据这个位置取出值
                        for (String column : columnName) {
                            if (nameIndexMap.containsKey(column)) {
                                Integer columnIndex = nameIndexMap.get(column);
                                field.set(obj, ConvertUtils.convert(row[columnIndex], filedType));
                                break;
                            }
                        }
                    }
                }
                list.add(obj);
            }
            System.out.println("所有数据解析完成！");

            excelInfo.setHeadMapList(headMapList);
            excelInfo.setHeadMap(headMap);
            excelInfo.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return excelInfo;
    }

    private static byte[] getBytes(InputStream source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = source.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos.toByteArray();
    }

    /**
     * 读取表格文件,支持xls、xlsx、csv，实体类需要有@ExcelProperty(value="xxx")的注解
     *
     * @param fileInputStream 文件流
     * @param headlineNum     标题行位置
     * @param clazz           封装对象
     * @param filePath        文件路径
     * @return 对象集合
     * @author yeweiwei
     * @date 2021/9/2 13:42
     */
    public static <T> ExcelInfo<T> readHeadMapListAndData(InputStream fileInputStream, int headlineNum,
                                                          Class<T> clazz, String filePath) {
        ExcelInfo<T> excelInfo = new ExcelInfo<>();
        //获取文件后缀，根据后缀名使用不同的方法读取
        if (filePath.contains(Constant.SplitChar.POINT_CHAR)) {
            String suffix = filePath.substring(filePath.lastIndexOf(Constant.SplitChar.POINT_CHAR) + 1);
            if (Strings.isNullOrEmpty(suffix)) {
                return excelInfo;
            }
            if (suffix.equals(Constant.Suffix.XLS) || suffix.equals(Constant.Suffix.XLSX)) {
                excelInfo = readExcelHeadMapListAndData(fileInputStream, headlineNum, clazz);
            } else if (suffix.equals(Constant.Suffix.CSV)) {
                excelInfo = readCsv(fileInputStream, headlineNum, clazz);
            }
        }
        return excelInfo;
    }
}
