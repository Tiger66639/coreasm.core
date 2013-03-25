package org.coreasm.eclipse.editors.warnings;

import org.coreasm.engine.CoreASMWarning;
import org.coreasm.engine.interpreter.Node;
import org.coreasm.engine.parser.CharacterPosition;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * This class represents warnings from CoreASM.
 * @author Michael Stegmaier
 *
 */
public class CoreASMEclipseWarning extends AbstractWarning {

	public CoreASMEclipseWarning(CoreASMWarning warning, IDocument document) {
		super("CoreASM Warning from " + warning.src + ": " + warning.message, "CoreASMWarning", calculatePosition(warning, document), calculateLength(warning.node));
	}

	private static int calculatePosition(CoreASMWarning warning, IDocument document) {
		CharacterPosition charPos = warning.pos;
		if (charPos != null) {
			try {
				return document.getLineOffset(charPos.line - 1) + charPos.column - 1;
			} catch (BadLocationException e) {
			}
		}
		Node node = warning.node;
		if (node != null)
			return node.getScannerInfo().charPosition;
		return 0;
	}
	
	private static int calculateLength(Node node) {
		if (node != null && node.getToken() != null)
			return node.getToken().length();
		return 0;
	}
}
