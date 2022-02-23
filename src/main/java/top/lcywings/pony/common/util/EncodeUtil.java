package top.lcywings.pony.common.util;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import java.io.*;

/**
 * @author huxubin
 * @version 1.0
 * @date 2020/10/31
 **/
public class EncodeUtil {

    private boolean found = false;
    private String encoding = null;

    private static final String WINDOWS_DEFAULT = "windows-1252";
    private static final String BIG5 = "Big5";

    /**
     * 传入一个文件(File)对象，检查文件编码
     *
     * @param file File对象实例
     * @return 文件编码，若无，则返回null
     */
    public String guessFileEncoding(File file) throws IOException {
        return guessFileEncoding(file, new nsDetector());
    }

    /**
     * 获取文件的编码
     *
     * @param file 文件
     * @param det  识别
     * @return 识别结果
     */
    public String guessFileEncoding(File file, nsDetector det) throws IOException {

        encoding = "GBK";
        det.Init(charset -> {
            encoding = charset;
            found = true;
        });
        //判断是否有BOM

        BOMInputStream bOMInputStream = new BOMInputStream(new FileInputStream(file));
        ByteOrderMark bom = bOMInputStream.getBOM();
        if (bom != null) {
            //如果有BOM，直接返回对应的类型
            return bom.getCharsetName();
        }
        BufferedInputStream imp = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int len;
        boolean done;

        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            done = det.DoIt(buf, len, false);
            if (done) {
                break;
            }
        }
        imp.close();
        det.DataEnd();

        if (!found) {
            String[] prob = det.getProbableCharsets();
            //可能有多个但只取第一个
            if (prob.length > 0) {
                // 在没有发现情况下,也可以只取第一个可能的编码,这里返回的是一个可能的序列
                return prob[0];
            } else {
                return null;
            }
        }
        return encoding;
    }

    /**
     * 获取文件的编码
     *
     * @param inputStream
     * @param det
     * @return
     * @throws IOException
     */
    public String guessFileEncodingByInputStream(InputStream inputStream, nsDetector det) throws IOException {
        encoding = "GBK";
        // Set an observer...
        // The Notify() will be called when a matching charset is found.
        det.Init(new nsICharsetDetectionObserver() {
            @Override
            public void Notify(String charset) {
                encoding = charset;
                found = true;
            }
        });

        //判断是否有BOM
        BOMInputStream bomInputStream = new BOMInputStream(inputStream);
        ByteOrderMark bom = bomInputStream.getBOM();
        if (bom != null) {
            //如果有BOM，直接返回对应的类型
            return bom.getCharsetName();
        }

        BufferedInputStream imp = new BufferedInputStream(inputStream);
        byte[] buf = new byte[1024];
        int len;
        boolean done;
        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            done = det.DoIt(buf, len, false);
            if (done) {
                break;
            }
        }
        imp.close();
        det.DataEnd();

        if (!found) {
            String[] prob = det.getProbableCharsets();
            //可能有多个但只取第一个
            if (prob.length > 0) {
                // 在没有发现情况下,也可以只取第一个可能的编码,这里返回的是一个可能的序列
                return prob[0];
            } else {
                return null;
            }
        }

        if (encoding.equals(WINDOWS_DEFAULT)) {
            return "GBK";
        }

        if(encoding.equalsIgnoreCase(BIG5)){
            return "GBK";
        }

        return encoding;
    }
}
