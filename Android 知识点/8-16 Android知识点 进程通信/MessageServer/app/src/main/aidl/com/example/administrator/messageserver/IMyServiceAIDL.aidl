// IMyServiceAIDL.aidl
package com.example.administrator.messageserver;

// Declare any non-default types here with import statements
import com.example.administrator.messageserver;
interface IMyServiceAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getValue(in Person person);
          Person getPerson();
}
