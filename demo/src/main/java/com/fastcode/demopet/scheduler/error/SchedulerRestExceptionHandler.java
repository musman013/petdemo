package com.fastcode.demopet.scheduler.error;

import com.fastcode.demopet.commons.error.ApiError;
import com.fastcode.demopet.commons.error.RestExceptionHandler;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class SchedulerRestExceptionHandler extends RestExceptionHandler {

	@Autowired
	private LoggingHelper logHelper;

	@ExceptionHandler(SchedulerException.class)
	public ResponseEntity<Object> handleSchedulerException(Exception ex, WebRequest request) {
		logHelper.getLogger().error("An Exception Occurred:", ex);
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}