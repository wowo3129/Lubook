package com.lubook.os.login.smsCheck.bean;

public class RespBean {

	public Resp resp;

	public class Resp {
		//
		// "respCode": "000000",
		// "templateSMS": {

		public String respCode;
		public TemplateSMS templateSMS;

		public class TemplateSMS {
			// "createDate": "20151223102143",
			// "smsId": "96544282fb642b3fa98163e77c0a3968"
			public String createDate;
			public String smsId;
		}
	}
}
