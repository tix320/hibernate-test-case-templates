package org.hibernate.bugs;

import org.hibernate.bytecode.enhance.spi.DefaultEnhancementContext;
import org.hibernate.bytecode.enhance.spi.UnloadedField;
import org.hibernate.bytecode.enhance.spi.UnsupportedEnhancementStrategy;

public class QuarkusLikeEnhancementContext extends DefaultEnhancementContext {
	@Override
	public boolean doBiDirectionalAssociationManagement(final UnloadedField field) {
		//Don't enable automatic association management as it's often too surprising.
		//Also, there's several cases in which its semantics are of unspecified,
		//such as what should happen when dealing with ordered collections.
		return false;
	}

	@Override
	public UnsupportedEnhancementStrategy getUnsupportedEnhancementStrategy() {
		// We expect model classes to be enhanced.
		// Lack of enhancement could lead to many problems,
		// from bad performance, to Quarkus-specific optimizations causing errors/data loss,
		// to incorrect generated bytecode (references to non-existing methods).
		// If something prevents enhancement, it's just safer to have Hibernate ORM's enhancer fail
		// with a clear error message pointing to the application class that needs to be fixed.
		return UnsupportedEnhancementStrategy.FAIL;
	}
}
