package com.npu.aoxiangbackend.dao;

import com.npu.aoxiangbackend.model.Response;
import java.util.List;
import java.util.Optional;

public interface IResponseDao {

    /**
     * 根据ID查找Response对象。
     * @param id 响应的ID。
     * @return 可能为空的Response对象。
     */
    Optional<Response> findResponseById(String id);

    /**
     * 根据调查ID和用户ID查找Response对象。
     * @param surveyId 调查的ID。
     * @param userId 用户的ID。
     * @return 可能为空的Response对象。
     */
    Optional<Response> findResponseBySurveyIdAndUserId(String surveyId, long userId);

    /**
     * 根据调查ID查找所有Response对象。
     * @param surveyId 调查的ID。
     * @return Response对象列表。
     */
    List<Response> findResponsesBySurveyId(String surveyId);

    /**
     * 根据用户ID查找所有Response对象。
     * @param userId 用户的ID。
     * @return Response对象列表。
     */
    List<Response> findResponsesByUserId(long userId);

    /**
     * 将指定的响应对象添加到数据库，返回自动创建的主键uuid。
     * @param response 响应对象。
     * @return 响应的uuid。
     */
    String addResponse(Response response);

    /**
     * 更新指定的响应对象。
     * @param response 响应对象。
     * @return 操作是否成功。
     */
    boolean updateResponse(Response response);

    /**
     * 删除指定ID的响应对象。
     * @param id 响应的ID。
     * @return 操作是否成功。
     */
    boolean deleteResponse(String id);
}