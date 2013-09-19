package sk.stuba.fiit.perconik.core.dom;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;
import com.google.common.annotations.Beta;

@Beta
public class GenericAstMatcher extends ASTMatcher
{
	public GenericAstMatcher(final boolean matchDocCommentTags)
	{
		super(matchDocCommentTags);
	}

	@SuppressWarnings("unused")
	public void preMatch(ASTNode node, Object other)
	{
		// default implementation does nothing
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean postMatch(ASTNode node, Object other, boolean result)
	{
		// default implementation does nothing
		return result;
	}
	
	@Override
	public boolean match(AnnotationTypeDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(AnnotationTypeMemberDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(AnonymousClassDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ArrayAccess node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ArrayCreation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ArrayInitializer node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ArrayType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(AssertStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(Assignment node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(Block node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(BlockComment node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(BooleanLiteral node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(BreakStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(CastExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(CatchClause node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(CharacterLiteral node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ClassInstanceCreation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(CompilationUnit node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ConditionalExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ConstructorInvocation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ContinueStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(UnionType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(DoStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(EmptyStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(EnhancedForStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(EnumConstantDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(EnumDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ExpressionStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(FieldAccess node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(FieldDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ForStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(IfStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ImportDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(InfixExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(InstanceofExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(Initializer node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(Javadoc node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(LabeledStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(LineComment node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MarkerAnnotation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MemberRef node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MemberValuePair node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MethodRef node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MethodRefParameter node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MethodDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(MethodInvocation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(Modifier node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(NormalAnnotation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(NullLiteral node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(NumberLiteral node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(PackageDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ParameterizedType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ParenthesizedExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(PostfixExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(PrefixExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(PrimitiveType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(QualifiedName node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(QualifiedType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ReturnStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SimpleName node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SimpleType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SingleMemberAnnotation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SingleVariableDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(StringLiteral node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SuperConstructorInvocation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SuperFieldAccess node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SuperMethodInvocation node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SwitchCase node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SwitchStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(SynchronizedStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TagElement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TextElement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ThisExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(ThrowStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TryStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TypeDeclaration node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TypeDeclarationStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TypeLiteral node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(TypeParameter node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(VariableDeclarationExpression node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(VariableDeclarationFragment node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(VariableDeclarationStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(WhileStatement node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}

	@Override
	public boolean match(WildcardType node, Object other)
	{
		this.preMatch(node, other);
		return this.postMatch(node, other, super.match(node, other));
	}
}