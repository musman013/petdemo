package com.fastcode.demopet.emailbuilder.application.mail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.EmailTemplateAppService;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.dto.CreateEmailTemplateInput;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.dto.FindEmailTemplateByNameOutput;
import com.fastcode.demopet.emailbuilder.application.emailvariable.EmailVariableAppService;
import com.fastcode.demopet.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;
import com.fastcode.demopet.emailbuilder.domain.irepository.IFileContentStore;
import com.fastcode.demopet.emailbuilder.domain.irepository.IFileRepository;
import com.fastcode.demopet.emailbuilder.domain.model.File;

@Service
public class EmailService implements IEmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private IFileContentStore contentStore;

	@Autowired
	private IFileRepository filesRepo;

	@Autowired
	private Environment env;

	@Autowired
	private EmailTemplateAppService _emailTemplateAppService;

	@Autowired
	private EmailVariableAppService _emailVariableAppService;

	public SimpleMailMessage buildEmail(String email, String appUrl, String resetCode)
	{
		SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
		passwordResetEmail.setTo(email);
		passwordResetEmail.setSubject("Password Reset Request");
		passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
				+ "/reset?token=" + resetCode);
		System.out.println("App url " + passwordResetEmail.getText());

		return passwordResetEmail;
	}

	@Async
	public void sendEmail(SimpleMailMessage email) {
		emailSender.send(email);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void sendMessage(String to, String cc, String bcc, String subject, String htmlContent,
			List<File> inlineImages, List<File> attachments) {

		MimeMessage message = emailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			String[] toArray = to.split(",", -1);
			String[] ccArray = cc != null ? cc.split(",", -1) : new String[0];
			String[] bccArray = bcc != null ? bcc.split(",", -1) : new String[0];

			helper.setTo(toArray);
			helper.setCc(ccArray);
			helper.setBcc(bccArray);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

			// Use the true flag to indicate the text included is HTML

			for (File file : inlineImages) {
				try {

					ByteArrayResource fileStreamResource = getFileStreamResource(Long.valueOf(file.getName()));
					if (fileStreamResource != null)
						helper.addInline(file.getSummary(), fileStreamResource, "image/jpeg");
				} catch (Exception e) {
					// ignore
					e.printStackTrace();
				}
			}

			// Now add the real attachments
			for (File file : attachments) {
				ByteArrayResource fileStreamResource = getFileStreamResource(file.getId());
				if (fileStreamResource != null)
					helper.addAttachment(file.getName(), fileStreamResource);
			}

		} catch (MessagingException ex) {
			ex.printStackTrace();
		}

		emailSender.send(message);
	}

	public ByteArrayResource getFileStreamResource(Long fileId) { // This method will download file using RestTemplate
		try {
			Optional<File> f = filesRepo.findById(fileId);
			// InputStreamResource inputStreamResource = new
			// InputStreamResource(contentStore.getContent(f.get()));
			InputStream content = contentStore.getContent(f.get());
			return content != null ? new ByteArrayResource(IOUtils.toByteArray(content)) : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String appendInlineImagePrifix(String name) {
		if (name.startsWith("cid:")) {
			return name;
		} else {
			return "cid:" + name;
		}
	}


	public void sendVisitEmail(FindEmailTemplateByNameOutput email,Map<String,String> emailVariableMap) throws IOException
	{

		if(email.getActive()!=null && email.getActive()) {
			String html = _emailTemplateAppService.convertJsonToHtml(replaceVisitVariableMap(email.getContentJson(),emailVariableMap));		
			email.setContentHtml(html);
			sendMessage(email.getTo(), null, null, email.getSubject(), email.getContentHtml(), new ArrayList<File>(),new ArrayList<File>());
		}
	}

	private String replaceVisitVariableMap(String input, Map<String, String> emailVariableMap) {
		HashMap<String, String> map = new HashMap<>();
		Set<String> variables = emailVariableMap.keySet();

		for (String variable: variables) {
			System.out.println(" KEY EXISTS " );
			map.put("{{" + variable + "}}",emailVariableMap.get(variable));

		}

		final String regex ="\\{\\{\\w+\\}\\}";// "\\{\\{\\w+}}"; //"{{\\w+}}";

		final Matcher m = Pattern.compile(regex).matcher(input);

		final List<String> matches = new ArrayList<>();
		while (m.find()) {
			matches.add(m.group(0));
			input = input.replaceAll(Pattern.quote(m.group(0)),map.get(m.group(0)));

		}
		System.out.println("\n\n input \n --" + input);
		return input;
	}

}
