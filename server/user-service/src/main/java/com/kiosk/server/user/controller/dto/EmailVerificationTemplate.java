package com.kiosk.server.user.controller.dto;

import lombok.Getter;

@Getter
public enum EmailVerificationTemplate {
    SUCCESS("""
       <!DOCTYPE html>
       <html>
       <head>
           <meta charset="UTF-8">
           <title>이메일 인증 완료</title>
           <style>
               body {
                   font-family: Arial, sans-serif;
                   display: flex;
                   justify-content: center;
                   align-items: center;
                   height: 100vh;
                   margin: 0;
                   background-color: #f5f5f5;
               }
               .container {
                   text-align: center;
                   padding: 2rem;
                   background-color: white;
                   border-radius: 8px;
                   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
               }
               h1 { color: #2C3E50; }
               p { color: #7F8C8D; }
           </style>
       </head>
       <body>
           <div class="container">
               <h1>이메일 인증이 완료되었습니다!</h1>
               <p>이제 모든 서비스를 이용하실 수 있습니다.</p>
               <p>앱으로 돌아가 서비스를 이용해주세요.</p>
           </div>
       </body>
       </html>
      \s"""),

    FAILURE("""
       <!DOCTYPE html>
       <html>
       <head>
           <meta charset="UTF-8">
           <title>이메일 인증 실패</title>
           <style>
               body {
                   font-family: Arial, sans-serif;
                   display: flex;
                   justify-content: center;
                   align-items: center;
                   height: 100vh;
                   margin: 0;
                   background-color: #f5f5f5;
               }
               .container {
                   text-align: center;
                   padding: 2rem;
                   background-color: white;
                   border-radius: 8px;
                   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
               }
               h1 { color: #e74c3c; }
               p { color: #7F8C8D; }
           </style>
       </head>
       <body>
           <div class="container">
               <h1>이메일 인증에 실패했습니다</h1>
               <p>인증 링크가 만료되었거나 잘못된 링크입니다.</p>
               <p>앱으로 돌아가 이메일 인증을 다시 시도해주세요.</p>
           </div>
       </body>
       </html>
       """);

    private final String template;

    EmailVerificationTemplate(String template) {
        this.template = template;
    }

}
