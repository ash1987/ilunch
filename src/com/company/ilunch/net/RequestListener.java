package com.company.ilunch.net;

public interface RequestListener<T> {
	public void OnStart();

	public void onError(Exception e);

	public void OnPaserComplete(T bean);
}
