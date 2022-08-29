package com.schilings.neiko.notify.service;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.notify.mapper.UserAnnouncementMapper;
import com.schilings.neiko.notify.model.entity.UserAnnouncement;
import org.springframework.stereotype.Service;

@Service
public class UserAnnouncementServiceImpl extends ExtendServiceImpl<UserAnnouncementMapper, UserAnnouncement> 
        implements UserAnnouncementService {
}
