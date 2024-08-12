package org.finos.springbot.tool.poll;

import org.finos.springbot.entityjson.EntityJson;
import org.finos.springbot.tool.poll.poll.Question;
import org.finos.springbot.workflow.content.User;
import org.finos.springbot.workflow.data.DataHandlerConfig;
import org.finos.springbot.workflow.data.EntityJsonConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
		DataHandlerConfig.class
})
public class TeamsDataConversionTest {
	
	public static final String SOME_JSON = """
			{
			  "formid" : "org.finos.springbot.tool.poll.poll.Question",
			  "buttons" : {
			    "type" : "org.finos.springbot.workflow.form.buttonList",
			    "version" : "1.0",
			    "contents" : [ {
			      "type" : "org.finos.springbot.workflow.form.button",
			      "version" : "1.0",
			      "name" : "org.finos.springbot.tool.poll.poll.PollController-poll0",
			      "buttonType" : "ACTION",
			      "text" : "kjh"
			    }, {
			      "type" : "org.finos.springbot.workflow.form.button",
			      "version" : "1.0",
			      "name" : "org.finos.springbot.tool.poll.poll.PollController-poll1",
			      "buttonType" : "ACTION",
			      "text" : "jh"
			    }, {
			      "type" : "org.finos.springbot.workflow.form.button",
			      "version" : "1.0",
			      "name" : "org.finos.springbot.tool.poll.poll.PollController-poll2",
			      "buttonType" : "ACTION",
			      "text" : "kkjh"
			    } ]
			  },
			  "form" : {
			    "type" : "org.finos.springbot.tool.poll.poll.question",
			    "version" : "1.0",
			    "question" : "hkjhjkj",
			    "options" : [ "kjh", "jh", "kkjh" ],
			    "id" : "ffe0d5988ab7",
			    "poller" : {
			      "type" : "org.finos.springbot.teams.content.teamsUser",
			      "version" : "1.0",
			      "aadObjectId" : "4a2777dd-0619-4f1e-b9d4-1112a701a40d",
			      "key" : "29:1nKqklVb4WIofhY4D5N3WoyKsl35ekq-qV-5lliPFJ-0E3RVd3gXp-ylXSKy9ixB3t2S-39Jc7lDztlD585iqOQ",
			      "name" : "Rob Moffat"
			    },
			    "endTime" : null
			  },
			  "header" : {
			    "type" : "org.finos.springbot.workflow.tags.headerDetails",
			    "version" : "1.0",
			    "name" : null,
			    "description" : null,
			    "tags" : [ "ffe0d5988ab7-q", "org-finos-springbot-tool-poll-poll-question" ]
			  },
			  "errors" : {
			    "type" : "org.finos.springbot.workflow.form.errorMap",
			    "version" : "1.0",
			    "contents" : { }
			  },
			  "storageId" : "9223370375352721108-8e7238cc-5f40-4a6d-a0ee-008e6ae96eff"
			}\
			""";
	
	@Autowired
	EntityJsonConverter ejc;
	
	@Test
	public void testDeserialize() {
		EntityJson data = ejc.readValue(SOME_JSON);
		Question q = (Question) data.get("form");
		User tu = q.getPoller();
		System.out.println(tu);
	}
	
	
}
