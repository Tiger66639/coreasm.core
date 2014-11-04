package org.coreasm.compiler.plugins.abstraction;

import org.coreasm.compiler.codefragment.CodeFragment;
import org.coreasm.compiler.exception.CompilerException;
import org.coreasm.engine.interpreter.ASTNode;
import org.coreasm.engine.plugins.abstraction.AbstractionPlugin;
import org.coreasm.compiler.CodeType;
import org.coreasm.compiler.CoreASMCompiler;
import org.coreasm.compiler.interfaces.CompilerCodeUPlugin;
import org.coreasm.compiler.interfaces.CompilerPlugin;

/**
 * Provides the abstract rule.
 * The abstract rule allows the user to leave out an actual implementation.
 * @author Markus Brenner
 *
 */
public class CompilerAbstractionPlugin implements CompilerCodeUPlugin, CompilerPlugin {

	@Override
	public String getName() {
		return AbstractionPlugin.PLUGIN_NAME;
	}

	@Override
	public CodeFragment uCode(ASTNode n)
			throws CompilerException {
		if(n.getGrammarClass().equals("Rule")){
			if(n.getGrammarRule().equals("AbstractRule")){
				CodeFragment result = new CodeFragment("");
				result.appendFragment(CoreASMCompiler.getEngine().compile(n.getAbstractChildNodes().get(0), CodeType.R));
				result.appendLine("@decl(String,msg)=evalStack.pop().toString();\n");
				result.appendLine("@decl(CompilerRuntime.UpdateList,ulist)=new CompilerRuntime.UpdateList();\n");
				result.appendLine("@ulist@.add(new CompilerRuntime.Update(plugins.IOPlugin.IOHelper.OUTPUT_FUNC_LOC, new plugins.StringPlugin.StringElement(\"Abstract Call: \" + @msg@), plugins.IOPlugin.IOHelper.PRINT_ACTION, this.getUpdateResponsible()));\n");
				result.appendLine("evalStack.push(@ulist@);\n");
				return result;
			}
		}

		throw new CompilerException(
				"unhandled code type: (AbstractionPlugin, uCode, "
						+ n.getGrammarClass() + ", " + n.getGrammarRule() + ")");
	}
}