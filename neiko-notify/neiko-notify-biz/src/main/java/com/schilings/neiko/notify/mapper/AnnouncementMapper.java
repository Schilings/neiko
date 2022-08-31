package com.schilings.neiko.notify.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.enums.BooleanEnum;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.query.LambdaQueryWrapperX;
import com.schilings.neiko.notify.enums.AnnouncementStatusEnum;
import com.schilings.neiko.notify.model.entity.Announcement;
import com.schilings.neiko.notify.model.entity.UserAnnouncement;
import com.schilings.neiko.notify.model.qo.AnnouncementQO;
import com.schilings.neiko.notify.model.vo.AnnouncementPageVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告信息
 */
public interface AnnouncementMapper extends ExtendMapper<Announcement> {

    /**
     * 分页查询
     * @param pageParam 分页参数
     * @param qo 查询对象
     * @return 分页结果数据 PageResult
     */
    default PageResult<AnnouncementPageVO> queryPage(PageParam pageParam, AnnouncementQO qo) {
        IPage<AnnouncementPageVO> page = this.prodPage(pageParam);
        NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                .selectAll(Announcement.class)
                .eq(Announcement::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
                .likeIfPresent(Announcement::getTitle, qo.getTitle())
                .inIfPresent(Announcement::getStatus, (Object[]) qo.getStatus())
                .eqIfPresent(Announcement::getRecipientFilterType, qo.getRecipientFilterType());
        IPage<AnnouncementPageVO> voPage = this.selectJoinPage(page, AnnouncementPageVO.class, AUTO_RESULT_MAP, queryWrapper);
        return this.prodPage(voPage);
    }

    /**
     * 更新公共（限制只能更新未发布的公共）
     * @param announcement 公共信息
     * @return 更新是否成功
     */
    default boolean updateIfUnpublished(Announcement announcement) {
        int flag = this.update(announcement,
                Wrappers.<Announcement>lambdaUpdate().eq(Announcement::getId, announcement.getId())
                        .eq(Announcement::getStatus, AnnouncementStatusEnum.UNPUBLISHED.getValue()));
        return SqlHelper.retBool(flag);
    }


    /**
     * 根据参数获取当前用户拉取过，或者未拉取过的有效的公告信息
     *
     * @param userId 用户ID
     * @param pulled 当前用户是否拉取过
     * @return 公告信息列表
     */
    default List<Announcement> listUserAnnouncements(Long userId, boolean pulled) {
        /*
        <select id="listUserAnnouncements" resultMap="mybatis-plus_Announcement">
		SELECT
		<include refid="Base_Alias_Column_List"/>
		FROM
		notify_announcement a
		LEFT JOIN notify_user_announcement ua
		ON ua.user_id = #{userId} AND ua.announcement_id = a.id
		WHERE
		a.STATUS = 1
		AND ( a.immortal = 1 OR a.deadline > now() )
		<choose>
			<when test="pulled">
				AND ua.id IS NOT NULL
			</when>
			<otherwise>
				AND ua.id IS NULL
			</otherwise>
		</choose>
	    </select>
         */
        NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                .selectAll(Announcement.class)
                .leftJoin(UserAnnouncement.class, UserAnnouncement::getAnnouncementId, Announcement::getId)
                .eq(Announcement::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
                .eq(UserAnnouncement::getUserId,userId)
                .eq(Announcement::getStatus, AnnouncementStatusEnum.ENABLED.getValue())
                .and(wrapper->{
                    wrapper.eq(Announcement::getImmortal, BooleanEnum.TRUE.getValue())
                            .or()
                            //mysql内置函数now()会被渲染为"now()",多了双引号，查询失败，待修改
                            .gt(Announcement::getDeadline, "2021-09-23 17:09:18");
                })
                .isNotNull(pulled, UserAnnouncement::getId)
                .isNull(!pulled, UserAnnouncement::getId);
        return this.selectJoinList(Announcement.class, AUTO_RESULT_MAP, queryWrapper);

    }

}
