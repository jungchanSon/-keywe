package com.kiosk.server.user.util;

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
        """),

    VERIFICATION_EMAIL("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', '맑은 고딕', sans-serif;
                    line-height: 1.6;
                    color: #333333;
                    margin: 0;
                    padding: 0;
                }
                .email-container {
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    background-color: #ffffff;
                }
                .header {
                    text-align: center;
                    padding: 20px 0;
                    border-bottom: 1px solid #eeeeee;
                }
                .content {
                    padding: 30px 0;
                    text-align: center;
                }
                .button {
                    display: inline-block;
                    padding: 12px 24px;
                    margin: 20px 0;
                    background-color: #4CAF50;
                    color: white !important;
                    text-decoration: none !important;
                    border-radius: 4px;
                    font-weight: bold;
                }
                .footer {
                    padding: 20px 0;
                    text-align: center;
                    font-size: 0.9em;
                    color: #666666;
                }
                .notice {
                    margin-top: 20px;
                    padding: 15px;
                    background-color: #f8f9fa;
                    border-radius: 4px;
                    font-size: 0.9em;
                    color: #666666;
                }
            </style>
        </head>
        <body style="font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', '맑은 고딕', sans-serif; line-height: 1.6; color: #333333; margin: 0; padding: 0;">
            <div class="email-container" style="max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff;">
                <div class="header" style="text-align: center; padding: 20px 0; border-bottom: 1px solid #eeeeee;">
                    <h2 style="margin: 0;">이메일 인증</h2>
                </div>
                <div class="content" style="padding: 30px 0; text-align: center;">
                    <p style="margin: 0;">회원가입을 완료하기 위해 아래 버튼을 클릭해주세요. <br>인증 링크는 30분간 유효합니다.</p>
        
                    <a href="%s" class="button" style="display: inline-block; padding: 12px 24px; margin: 20px 0; background-color: #4CAF50; color: white !important; text-decoration: none !important; border-radius: 4px; font-weight: bold;">이메일 인증하기</a>
        
                    <div class="notice" style="margin-top: 20px; padding: 15px; background-color: #f8f9fa; border-radius: 4px; font-size: 0.9em; color: #666666;">
                        <p style="margin: 0;">본 메일은 발신전용이며 회신이 되지 않습니다.</p>
                    </div>
                </div>
                <div class="footer" style="padding: 20px 0; text-align: center; font-size: 0.9em; color: #666666;">
                </div>
            </div>
        </body>
        </html>
        """);

    private final String template;

    EmailVerificationTemplate(String template) {
        this.template = template;
    }

}
