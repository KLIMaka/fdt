package ssl.compilers;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.console.MessageConsoleStream;

public interface ISSLCompiler {
    String getName();

    boolean check(String path);

    Mark[] compile(File compPath, IFile resource, File outFile, MessageConsoleStream msg) throws Exception;
}
