package com.oeong.controller;

import com.oeong.entity.User;
import com.oeong.entity.UserFile;
import com.oeong.service.UserFileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private UserFileService userFileService;

    @GetMapping("/index")
    public String fileIndex() {
        return "file/list";
    }

    @PostMapping("/all")
    @ResponseBody
    public Map<String, Object> queryAllFile(HttpSession session, HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        User user = (User) session.getAttribute("user");
        List<UserFile> files = userFileService.queryByUserId(user.getId(), page, limit);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("count", userFileService.queryFileCounts(user.getId()));
        res.put("data", files);
        return res;
    }

    // 上传文件
    @PostMapping("/upload")
    @ResponseBody // 将Java对象转为Json格式的数据
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, HttpSession session) {
        System.out.println("upload trigger");
        Map<String, String> res = new HashMap<>();
        try {
            // 获取用户
            User user = (User) session.getAttribute("user");
            // 获取文件原始名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            // 获取文件大小
            long size = file.getSize();
            // 获取文件类型
            String type = file.getContentType();
            // 根据日期动态生成目录
            String localContainer = "/fileContainer";
            String uploadPath = ResourceUtils.getURL("classpath:").getPath() + localContainer;
            String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File dateDirPath = new File(uploadPath + File.separator + dateFormat);
            if (!dateDirPath.exists()) {
                dateDirPath.mkdirs();
            }
            System.out.println(dateDirPath);
            // 处理文件上传
            file.transferTo(new File(dateDirPath, fileName));
            // 将文件信息存入数据库中
            UserFile userFile = new UserFile();
            userFile.setFileName(fileName)
                    .setExt("." + extension)
                    .setPath(Paths.get(localContainer, dateFormat, fileName).toString())
                    .setSize(size)
                    .setType(type)
                    .setUserId(user.getId());
            userFileService.save(userFile);
            res.put("code", "0");
            res.put("msg", "上传成功");
            res.put("url", "/fileStorage/file/index");
        } catch (IOException e) {
            e.printStackTrace();
            res.put("code", "-1");
            res.put("msg", "上传失败");
            res.put("url", "/fileStorage/file/index");
        }
        return res;
    }


}
