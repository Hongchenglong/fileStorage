package com.oeong.service.impl;

import com.oeong.entity.UserFile;
import com.oeong.service.UserFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Hongchenglong
 * @Date: 2021/6/23 14:25
 * @Description:
 */
@SpringBootTest
class UserFileServiceImplTest {

    @Autowired
    private UserFileService userFileService;

    @Test
    public void test() {
        UserFile userFile = userFileService.queryByUserFileId(52);
        System.out.println(userFile);
    }
}