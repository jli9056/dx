package com.dixin.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dixin.annotation.Aggregate;

public class AggregateFactory {
	private static AggregateFactory instance=new AggregateFactory();
	
	public static AggregateFactory getInstance() {
		return instance;
	}

	public Aggregate[] getAggregateProperties(Class<?> c){
		List<Aggregate> alist = new ArrayList<Aggregate>();
		Method[] ms =c.getMethods();
		for(Method m:ms){
			Aggregate a =m.getAnnotation(Aggregate.class);
			if(a!=null){
				alist.add(a);
			}
		}
		return alist.toArray(new Aggregate[alist.size()]);
	}
}
