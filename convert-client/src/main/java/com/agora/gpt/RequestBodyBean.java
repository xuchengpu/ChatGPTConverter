package com.agora.gpt;

/**
 * Created by 许成谱 on 4/13/23 3:19 下午.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class RequestBodyBean {

   private FixSizeList<Messages> messages;

   public FixSizeList<Messages> getMessages() {
      return messages;
   }

   public void setMessages(FixSizeList<Messages> messages) {
      this.messages = messages;
   }

   public static class Messages {
      private String role;
      private String content;

      public String getRole() {
         return role;
      }

      public void setRole(String role) {
         this.role = role;
      }

      public String getContent() {
         return content;
      }

      public void setContent(String content) {
         this.content = content;
      }
   }
}
