/*******************************************************************************
 * Copyright (c) 2012, 2020 Original authors and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package aero.minova.test.saw.rcp.dataset.person;

import java.util.Date;

public class PersonWithAddress extends Contact {

    private Address address;

    public PersonWithAddress(
            int id,
            String firstName,
            String lastName,
            Gender gender,
            boolean married,
            Date birthday,
            Address address) {
        super(id, firstName, lastName, gender, married, birthday);
        this.address = address;
    }

    public PersonWithAddress(Contact person, Address address) {
        super(person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getGender(),
                person.isMarried(),
                person.getBirthday());
        this.address = address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return this.address;
    }

}
