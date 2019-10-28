package com.symphony.spring.app;

import org.springframework.web.servlet.View;

import com.symphony.id.SymphonyIdentity;

public abstract class AbstractJsonController extends AbstractController {

	protected View v;
	protected SymphonyIdentity appIdentity;
	
	public AbstractJsonController(SymphonyAppProperties p, View v, SymphonyIdentity id) {
		super(p);
		this.v = v;
		this.appIdentity = id;
	}
}