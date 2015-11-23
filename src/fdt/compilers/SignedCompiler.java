package fdt.compilers;

import java.io.*;

public abstract class SignedCompiler implements ISSLCompiler {
	@Override
	public boolean check(String path) {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(path);
			BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			return checkSignature(r);
		} catch (Exception e) {
		}
		return false;
	}

	protected abstract boolean checkSignature(BufferedReader r) throws IOException;

}
