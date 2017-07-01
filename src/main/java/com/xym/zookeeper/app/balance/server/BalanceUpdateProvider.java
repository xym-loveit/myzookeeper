package com.xym.zookeeper.app.balance.server;

public interface BalanceUpdateProvider {
	
	public boolean addBalance(Integer step);
	
	public boolean reduceBalance(Integer step);

}
