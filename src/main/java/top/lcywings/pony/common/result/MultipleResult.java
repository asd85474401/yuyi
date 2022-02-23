package top.lcywings.pony.common.result;

import top.lcywings.pony.common.constant.Vm;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Data
public class MultipleResult<T> {

    /**
     * 状态
     */
    private boolean success;

    /**
     * 返回消息
     */
    private String msg = "";

    /**
     * 数据
     */
    private List<T> data;

    /**
     * 返回链接
     */
    private String url = "";

    /**
     * 空数据的返回
     *
     * @author huxubin
     * @date 2021/11/9 19:59
     */
    public void empty() {
        this.msg = Vm.NO_DATA;
        this.success = true;
        this.data = Lists.newArrayList();
    }

    /**
     * 错误数据的返回
     *
     * @author huxubin
     * @date 2021/11/9 19:59
     */
    public MultipleResult<T> error(String message) {
        this.msg = message;
        this.success = false;
        this.data = Lists.newArrayList();
        return this;
    }

    /**
     * 成功的消息返回
     *
     * @param data 返回的数据
     * @author huxubin
     * @date 2021/11/9 20:00
     */
    public MultipleResult<T> success(List<T> data) {
        this.msg = Vm.SUCCESS_MSG;
        this.success = true;
        this.data = data;
        return this;
    }

}
