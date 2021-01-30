package com.noahedu.dao;

import java.util.List;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：TaskMapper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:18$
 */
public interface TaskMapper {
    List<TaskVo> findTask(TaskVo vo);
    List<TaskVo> findById(TaskVo vo);
    List<TaskVo> findByRequestCode(TaskVo vo);
    void insert(TaskVo vo);
    void update(TaskVo vo);
    void updateRun(TaskVo vo);
    void delete(TaskVo vo);

}
