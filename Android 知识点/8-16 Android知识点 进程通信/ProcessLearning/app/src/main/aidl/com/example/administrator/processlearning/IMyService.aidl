// IMyService.aidl
package com.example.administrator.processlearning;


// Declare any non-default types here with import statements

import com.example.administrator.processlearning.Person1;
interface IMyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      String getValue(in Person1 person);
      Person1 getPerson();

}
