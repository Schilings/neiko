package com.schilings.neiko.notify.service.impl;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.notify.mapper.AnnouncementMapper;
import com.schilings.neiko.notify.model.entity.Announcement;
import com.schilings.neiko.notify.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ExtendServiceImpl<AnnouncementMapper, Announcement>
        implements AnnouncementService {
}
