/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.intkey.directives;

import au.org.ala.delta.intkey.directives.invocation.DisplayCommentsDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.OnOffDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class DisplayCommentsDirective extends OnOffDirective {

    public DisplayCommentsDirective() {
        super(false, "display", "comments");
    }

    @Override
    public OnOffDirectiveInvocation buildCommandObject() {
        return new DisplayCommentsDirectiveInvocation();
    }

    @Override
    boolean getInitialOnOffValue(IntkeyContext context) {
        return context.displayComments();
    }

}
