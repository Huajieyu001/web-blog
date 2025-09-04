package top.huajieyu001.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.huajieyu001.blog.service.OssService;

@RestController
@RequestMapping("/oss")
@AllArgsConstructor
public class OssController {

    @Autowired
    private final OssService ossService;

    @GetMapping("/generatePostSignature")
    @ResponseBody
    public String generatePostSignature() {
        return ossService.generatePostSignature();
    }
}
