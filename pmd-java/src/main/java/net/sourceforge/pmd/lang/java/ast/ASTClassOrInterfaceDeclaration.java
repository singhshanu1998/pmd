/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import java.util.List;

import net.sourceforge.pmd.annotation.Experimental;
import net.sourceforge.pmd.lang.ast.Node;


/**
 * Represents class and interface declarations. This is a {@linkplain Node#isFindBoundary() find boundary}
 * for tree traversal methods.
 *
 * <pre class="grammar">
 *
 * ClassOrInterfaceDeclaration ::= {@link ASTModifierList ModifierList}
 *                                 ( "class" | "interface" )
 *                                 &lt;IDENTIFIER&gt;
 *                                 {@link ASTTypeParameters TypeParameters}?
 *                                 {@link ASTExtendsList ExtendsList}?
 *                                 {@link ASTImplementsList ImplementsList}?
 *                                 {@link ASTClassOrInterfaceBody ClassOrInterfaceBody}
 *
 * </pre>
 */
public final class ASTClassOrInterfaceDeclaration extends AbstractAnyTypeDeclaration {

    private boolean isInterface;

    ASTClassOrInterfaceDeclaration(int id) {
        super(id);
    }

    @Override
    protected <P, R> R acceptVisitor(JavaVisitor<? super P, ? extends R> visitor, P data) {
        return visitor.visit(this, data);
    }

    @Override
    public boolean isPackagePrivate() {
        return super.isPackagePrivate() && !isLocal();
    }

    @Override
    public boolean isInterface() {
        return this.isInterface;
    }

    void setInterface() {
        this.isInterface = true;
    }


    /**
     * Returns the superclass type node if this node is a class
     * declaration and explicitly declares an {@code extends}
     * clause. Superinterfaces of an interface are not considered.
     *
     * <p>Returns {@code null} otherwise.
     */
    public ASTClassOrInterfaceType getSuperClassTypeNode() {
        if (isInterface()) {
            return null;
        }

        ASTExtendsList extendsList = getFirstChildOfType(ASTExtendsList.class);
        return extendsList == null ? null : extendsList.iterator().next();
    }


    @Experimental
    public List<ASTClassOrInterfaceType> getPermittedSubclasses() {
        return ASTList.orEmpty(children(ASTPermitsList.class).first());
    }

}
