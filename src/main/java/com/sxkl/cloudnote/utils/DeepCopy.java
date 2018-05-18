package com.sxkl.cloudnote.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.Cleanup;

public class DeepCopy<V> {

	@SuppressWarnings("unchecked")
	public V copy(V v) {
		try {
			@Cleanup
			ByteArrayOutputStream bos = null;
			@Cleanup
			ObjectOutputStream oos = null;
			@Cleanup
			ObjectInputStream ois = null;
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(v);
			ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			return (V) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
