package com.github.deutschebank.symphony.stream.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.deutschebank.symphony.stream.log.SharedLog;
import com.github.deutschebank.symphony.stream.log.SharedLogMessage;
import com.github.deutschebank.symphony.stream.log.SharedLogMessageType;
import com.github.deutschebank.symphony.stream.msg.Participant;

/**
 * Implementation of an untrustworthy log, mocking a symphony room.
 * 
 * @author robmoffat
 *
 */
public class ListBackedSharedLog implements SharedLog {

	private Connectivity conn;
	private List<SharedLogMessage> messages = new ArrayList<>();

	public ListBackedSharedLog(Connectivity conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void writeLeaderMessage(Participant p) {
		checkIsolation(p);

		SharedLogMessage message = new SharedLogMessage(p, SharedLogMessageType.LEADER);
		messages.add(message);
		System.out.println(message);
	}

	protected void checkIsolation(Participant p) {
		conn.checkIsolated(p);
		conn.checkWriteDown();
	}

	@Override
	public void writeParticipantMessage(Participant p) {
		checkIsolation(p);
		SharedLogMessage message = new SharedLogMessage(p, SharedLogMessageType.PARTICIPANT);
		messages.add(message);
		System.out.println(message);
	}

	@Override
	public List<Participant> getRegisteredParticipants(Participant p) {
		checkIsolation(p);
		return messages.stream().map(m -> m.getParticipant()).distinct().collect(Collectors.toList());
	}

	@Override
	public Optional<Participant> getLeader(Participant me) {
		checkIsolation(me);
		return messages.stream()
			.filter(m -> m.getMessageType() == SharedLogMessageType.LEADER)
			.map(m -> m.getParticipant())
			.reduce((first, second) -> second);
	}

}
