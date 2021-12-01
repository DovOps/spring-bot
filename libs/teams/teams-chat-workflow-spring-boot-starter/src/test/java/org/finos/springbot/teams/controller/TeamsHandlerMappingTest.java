package org.finos.springbot.teams.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.finos.springbot.teams.MockTeamsConfiguration;
import org.finos.springbot.teams.TeamsWorkflowConfig;
import org.finos.springbot.teams.content.TeamsMultiwayChat;
import org.finos.springbot.teams.messages.MessageActivityHandler;
import org.finos.springbot.teams.turns.CurrentTurnContext;
import org.finos.springbot.tests.controller.AbstractHandlerMappingTest;
import org.finos.springbot.tests.controller.OurController;
import org.finos.springbot.workflow.actions.Action;
import org.finos.springbot.workflow.actions.SimpleMessageAction;
import org.finos.springbot.workflow.annotations.ChatRequest;
import org.finos.springbot.workflow.annotations.WorkMode;
import org.finos.springbot.workflow.content.Message;
import org.finos.springbot.workflow.form.Button;
import org.finos.springbot.workflow.form.Button.Type;
import org.finos.springbot.workflow.form.ButtonList;
import org.finos.springbot.workflow.java.mapping.ChatMapping;
import org.finos.springbot.workflow.java.mapping.ChatRequestChatHandlerMapping;
import org.finos.springbot.workflow.response.WorkResponse;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ActivityTypes;
import com.microsoft.bot.schema.Attachment;
import com.microsoft.bot.schema.ChannelAccount;
import com.microsoft.bot.schema.ConversationAccount;
import com.microsoft.bot.schema.Entity;
import com.microsoft.bot.schema.Mention;
import com.microsoft.bot.schema.teams.ChannelInfo;
import com.microsoft.bot.schema.teams.TeamsChannelData;


@SpringBootTest(classes = {
		MockTeamsConfiguration.class, 
		TeamsWorkflowConfig.class,
})
@ActiveProfiles("teams")
public class TeamsHandlerMappingTest extends AbstractHandlerMappingTest {
	
	ArgumentCaptor<Activity> msg;
	TurnContext tc;
	
	@Autowired
	MessageActivityHandler mah;
	
	@Autowired
	ChatRequestChatHandlerMapping hm;
	
	
    public static void compareJson(String loadJson, String json) throws JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Assertions.assertEquals(om.readTree(loadJson), om.readTree(json));
    }


	protected WorkResponse createWorkAddSubmit(WorkMode wm, Object ob5) {
		tc = Mockito.mock(TurnContext.class);
		CurrentTurnContext.CURRENT_CONTEXT.set(tc);
		msg = ArgumentCaptor.forClass(Activity.class);
		Mockito.when(tc.sendActivity(msg.capture())).thenReturn(CompletableFuture.completedFuture(null));		
		TeamsMultiwayChat theRoom = new TeamsMultiwayChat( "abc123", "tesxt room");
		WorkResponse wr = new WorkResponse(theRoom, ob5, wm);
		ButtonList bl = (ButtonList) wr.getData().get(ButtonList.KEY);
		Button submit = new Button("submit", Type.ACTION, "GO");
		bl.add(submit);
		return wr;
	}
	

	@Override
	protected String getMessageData() {
		Activity out = msg.getValue();
		if (out.getAttachments().size() > 0) {
			Attachment a1 = out.getAttachments().get(0);
			try {
				return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(a1.getContent());
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			
			
		} else {
			return "";
		}
	}


	@Override
	protected String getMessageContent() {
		Activity out = msg.getValue();
		if (out.getAttachments().size() > 0) {
			Attachment a1 = out.getAttachments().get(0);
			return (String) a1.getContent();
			
			
		} else {
			return out.getText();
		}
	}


	@Override
	protected void execute(String s) throws Exception {
		s = s.replace("@gaurav", "<span itemscope=\"\" itemtype=\"http://schema.skype.com/Mention\" itemid=\"0\">Gaurav</span>");
		tc = Mockito.mock(TurnContext.class);
		CurrentTurnContext.CURRENT_CONTEXT.set(tc);
		msg = ArgumentCaptor.forClass(Activity.class);
		Mockito.when(tc.sendActivity(msg.capture())).thenReturn(CompletableFuture.completedFuture(null));
		
		Activity out = new Activity(ActivityTypes.MESSAGE);
		Attachment a = new Attachment();
		a.setContentType(MediaType.TEXT_HTML_VALUE);
		a.setContent("<div>"+s+"</div>");
		out.setAttachment(a);
		
		ConversationAccount conv = new ConversationAccount(ROB_EXAMPLE_EMAIL);
		out.setConversation(conv);
		
		TeamsChannelData tcd = new TeamsChannelData();
		ChannelInfo ci = new ChannelInfo(CHAT_ID, OurController.SOME_ROOM);
		tcd.setChannel(ci);
		out.setChannelData(tcd);
		
		ChannelAccount ca = new ChannelAccount(""+ROB_EXAMPLE_ID, ROB_NAME);
		out.setFrom(ca);
		
		ChannelAccount to = new ChannelAccount(""+BOT_ID, BOT_NAME);
		out.setRecipient(to);
		
		out.setEntities(Arrays.asList(gauravEntity()));
		
		
		Mockito.when(tc.getActivity()).thenReturn(out);
		
		mah.onTurn(tc);
	}


	private Entity gauravEntity() {
		Mention out = new Mention();
		out.setText("<at>Gaurav</at>");
		ChannelAccount ca = new ChannelAccount();
		ca.setName("Gaurav P");
		ca.setId("3276423876");
		out.setMentioned(ca);
		return new Entity().setAs(out);
	}


	@Override
	protected void pressButton(String s) {
//		EntityJson jsonObjects = new EntityJson();
//		jsonObjects.put("1", new SymphonyUser(123l, "gaurav", "gaurav@example.com"));
//		jsonObjects.put("2", new HashTag("SomeTopic"));
//		Chat r = new SymphonyRoom("The Room Where It Happened", "abc123");
//		User author = new SymphonyUser(ROB_EXAMPLE_ID, ROB_NAME, ROB_EXAMPLE_EMAIL);
//		Object fd = new StartClaim();
//		Action a = new FormAction(r, author, fd, s, jsonObjects);
//		Action.CURRENT_ACTION.set(a);
//		mc.accept(a);
	}


	@Override
	protected List<ChatMapping<ChatRequest>> getMappingsFor(Message s) throws Exception {
		Map<String, Object> map = new HashMap<>();
 		Action a = new SimpleMessageAction(null, null, s, map);
		return hm.getHandlers(a);
	}


	@Override
	protected void assertHelpResponse() throws Exception {
		String data = getMessageData();
		System.out.println(data);
		Assertions.assertTrue(data.contains(" - ${string($data)}\","));
	}


	@Override
	protected void assertNoButtons() {
		String data = getMessageData();
		Assertions.assertFalse(data.contains("ActionSet"));
	}

}
