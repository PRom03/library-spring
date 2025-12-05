//package org.example.library.Controllers;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class MyErrorController implements ErrorController {
//
//    @RequestMapping("/error")
//    public ResponseEntity<Void> handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            int code = Integer.parseInt(status.toString());
//            if (code == 404) {
//                return ResponseEntity.status(404).build();
//            }
//        }
//
//        return ResponseEntity.status(500).build();
//    }
//}
