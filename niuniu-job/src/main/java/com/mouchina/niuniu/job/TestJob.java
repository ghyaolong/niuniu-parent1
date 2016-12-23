package com.mouchina.niuniu.job;

import org.springframework.stereotype.Component;

/**
 * 任务：广告解冻
 * @author hpf
 *
 */
@Component
public final class TestJob extends ABaseJob{
	public void execute(){
		System.out.println("test -------------------------" + TestJob.class.getName());
	}
}
