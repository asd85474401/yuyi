package com.kcidea.erms.service.database.impl;

import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.SortUrlUtil;
import com.kcidea.erms.dao.database.DatabaseKeyDao;
import com.kcidea.erms.domain.database.DatabaseKey;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.database.DataBaseSortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/13
 **/
@Slf4j
@Service
public class DataBaseSortUrlServiceImpl extends BaseService implements DataBaseSortUrlService {

    @Resource
    private DatabaseKeyDao databaseKeyDao;

    @Value("${my-config.web-url}")
    private String WEB_URL;

    /**
     * 查询数据库的短链接
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/13 13:54
     */
    @Override
    public String findDataBaseSortUrl(Long sid, Long did) {

        //查询数据库的apiKey
        DatabaseKey databaseKey = super.findOneDataBaseKeyBySidDid(sid, did);

        //如果原来已经生成了短链接，就用原来的
        if (!Strings.isNullOrEmpty(databaseKey.getSortUrl())) {
            return databaseKey.getSortUrl();
        }

        //数据库的apiKey
        String apiKey = databaseKey.getApiKey();

        //原来没有，需要请求接口生成一个新的
//        String sortUrl = SortUrlUtil.createSortUrl(apiKey);

        String sortUrl = WEB_URL.concat(apiKey);

        if (Strings.isNullOrEmpty(sortUrl)) {
            throw new CustomException("生成链接失败，请稍后重试");
        }

        databaseKey.setSortUrl(sortUrl);
        databaseKeyDao.updateById(databaseKey);
        return sortUrl;
    }

    /**
     * 更新数据库的短链接
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/13 13:56
     */
    @Override
    public String updateDataBaseSortUrl(Long sid, Long did) {

        //查询数据库的apiKey
        DatabaseKey databaseKey = super.findOneDataBaseKeyBySidDid(sid, did);

        //当前时间
        LocalDateTime now = LocalDateTime.now();

        //如果有上次更新时间，需要进行判断，先去掉，等确认使用短链接再放出来
//        if (databaseKey.getUpdatedTime() != null) {
//            //如果距离上次更新时间不足24小时，那就不能刷新
//            if (databaseKey.getUpdatedTime().plusDays(1).isAfter(LocalDateTime.now())) {
//                throw new CustomException("很抱歉，距离上次刷新时间不足24小时，无法进行刷新");
//            }
//        }

        //生成一个新的apiKey
        String apiKey = UUID.randomUUID().toString().replace("-", "");

        //根据新的apiKey，生成新的短链接
//        String sortUrl = SortUrlUtil.createSortUrl(apiKey);

        String sortUrl = WEB_URL.concat(apiKey);

        if (Strings.isNullOrEmpty(sortUrl)) {
            throw new CustomException("生成链接失败，请稍后重试");
        }

        databaseKey.setApiKey(apiKey).setSortUrl(sortUrl).setUpdatedTime(now);
        databaseKeyDao.updateById(databaseKey);

        return sortUrl;
    }

    /**
     * 根据学校id和数据库id查询对应的apiKey
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库的apiKey
     * @author huxubin
     * @date 2022/2/9 10:18
     */
    @Override
    public String findApiKey(Long sid, Long did) {
        return super.findDataBaseApiKeyBySidDid(sid, did);
    }

    /**
     * 更新数据库的apiKey
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 更新后的apiKey
     * @author huxubin
     * @date 2022/2/9 10:18
     */
    @Override
    public String updateApiKey(Long sid, Long did) {

        //查询数据库的apiKey
        DatabaseKey databaseKey = super.findOneDataBaseKeyBySidDid(sid, did);

        //生成一个新的apiKey
        String apiKey = UUID.randomUUID().toString().replace("-", "");

        //短链接
        String sortUrl = WEB_URL.concat(apiKey);

        databaseKey.setApiKey(apiKey).setSortUrl(sortUrl).setUpdatedTime(LocalDateTime.now());
        databaseKeyDao.updateById(databaseKey);

        return apiKey;
    }

    /**
     * 保存登录的apiKey
     *
     * @param ip     ip地址
     * @param apiKey apiKey
     * @return 保存的结果
     * @author huxubin
     * @date 2022/2/9 14:58
     */
    @Override
    public String saveApiKeyLogin(String ip, String apiKey) {

        if (Strings.isNullOrEmpty(apiKey)) {
            throw new CustomException("很抱歉，请输入apiKey");
        }

        DatabaseKey databaseKey = databaseKeyDao.findOneByApiKey(apiKey);
        if (databaseKey == null) {
            throw new CustomException("很抱歉，您输入的apiKey无效，请联系老师获取正确的apiKey");
        }

        return Vm.UPDATE_SUCCESS;
    }


}
