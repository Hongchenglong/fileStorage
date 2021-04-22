package com.oeong.controller;

import com.oeong.entity.User;
import com.oeong.entity.UserFile;
import com.oeong.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

}
