package com.fastcode.demo.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "apihistory", schema = "sample")
public class ApiHistoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6336351395990324861L;
	private Long id;
	private String correlation;
	private String path;
	private long processTime;
	private LocalDateTime requestTime;
	private LocalDateTime responseTime;
	private String responseStatus;
	private String userName;
	private String method;
	private String contentType;
	private String query;
	private String clientAddress;
	private String scheme;
	private String header;
	private String body;
	private String browser;
	private String response;

	@Id
	@Column(name = "Id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorrelation() {
		return correlation;
	}

	public void setCorrelation(String correlation) {
		this.correlation = correlation;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Basic
	@Column(name = "UserName", nullable = true, length = 32)

	@Length(max = 32, message = "Username must be less than 32 characters")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Basic
	@Column(name = "method", nullable = true, length = 10)
	@Length(max = 10, message = "method must be less than 10 characters")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Basic
	@Column(name = "ContentType", nullable = true, length = 100)
	@Length(max = 100, message = "ContentType must be less than 100 characters")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Basic
	@Column(name = "query", nullable = true, length = 1000)
	@Length(max = 1000, message = "query must be less than 1000 characters")
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Basic
	@Column(name = "ClientAddress", nullable = true, length = 100)
	@Length(max = 100, message = "clientAddress must be less than 100 characters")
	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	@Basic
	@Column(name = "scheme", nullable = true, length = 100)
	@Length(max = 100, message = "scheme must be less than 100 characters")
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@Basic
	@Column(name = "header", nullable = true, length = 2000)
	@Length(max = 2000, message = "Header must be less than 2000 characters")
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Basic
	@Column(name = "body", nullable = true, length = 4000)
	@Length(max = 4000, message = "Body must be less than 4000 characters")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Basic
	@Column(name = "ProcessTime")
	public long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}

	@Basic
	@Column(name = "RequestTime")
	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	@Basic
	@Column(name = "ResponseTime")
	public LocalDateTime getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(LocalDateTime responseTime) {
		this.responseTime = responseTime;
	}

	@Basic
	@Column(name = "ResponseStatus", length = 5)
	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	@Basic
	@Column(name = "browser", length = 1000)
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	@Basic
	@Column(name = "response", length = 1000)
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ApiHistoryEntity))
			return false;
		ApiHistoryEntity obj = (ApiHistoryEntity) o;
		return id != null && id.equals(obj.id);
	}

	@Override
	public int hashCode() {
		return 131;
	}

}