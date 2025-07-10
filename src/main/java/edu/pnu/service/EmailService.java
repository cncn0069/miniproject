package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import edu.pnu.domain.OrderItem;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {
	private final JavaMailSender mailSender;
	
	public void sendPaymentInfoMail(String to, String customerName, Long amount, LocalDateTime paymentDate, List<OrderItem> orderItemList) {
	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        StringBuilder htmlContent = new StringBuilder();
	        htmlContent.append("<html><body>")
	            .append("<h2 style='background:#4CAF50;color:#fff;padding:16px;text-align:center;'>쓰레기 처리 결제 완료 안내</h2>")
	            .append("<p>아래와 같이 결제 내역이 접수되었습니다.</p>")
	            .append("<table style='width:100%;border-collapse:collapse;margin:24px 0;'>")
	            .append("<tr><th style='border:1px solid #ddd;padding:8px;background:#f2f2f2;'>고객명</th>")
	            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(customerName).append("</td></tr>")
	            .append("<tr><th style='border:1px solid #ddd;padding:8px;background:#f2f2f2;'>결제금액</th>")
	            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(amount).append(" 원</td></tr>")
	            .append("<tr><th style='border:1px solid #ddd;padding:8px;background:#f2f2f2;'>결제일시</th>")
	            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(paymentDate.toString()).append("</td></tr>")
	            .append("</table>");

	        // 처리 내역(주문 항목) 테이블 추가
	        htmlContent.append("<h3>처리 내역</h3>")
	            .append("<table style='width:100%;border-collapse:collapse;'>")
	            .append("<tr>")
	            .append("<th style='border:1px solid #ddd;padding:8px;background:#f2f2f2;'>항목명</th>")
	            .append("<th style='border:1px solid #ddd;padding:8px;background:#f2f2f2;'>금액</th>")
	            .append("</tr>");

	        for (OrderItem item : orderItemList) {
	            htmlContent.append("<tr>")
	                .append("<td style='border:1px solid #ddd;padding:8px;'>").append(item.getItemName()).append("</td>")
	                .append("<td style='border:1px solid #ddd;padding:8px;'>").append(item.getItemPrice()).append(" 원</td>")
	                .append("</tr>");
	        }

	        htmlContent.append("</table>")
	            .append("<p>확인 후 신속히 처리 부탁드립니다.</p>")
	            .append("<div style='color:#888;font-size:12px;text-align:center;margin-top:32px;'>본 메일은 자동 발송되었습니다.</div>")
	            .append("</body></html>");

	        helper.setTo(to);
	        helper.setSubject("쓰레기 처리 결제 완료 안내");
	        helper.setText(htmlContent.toString(), true); // true: HTML 적용
	        mailSender.send(message);
	        log.info("메일전송 완료!");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
