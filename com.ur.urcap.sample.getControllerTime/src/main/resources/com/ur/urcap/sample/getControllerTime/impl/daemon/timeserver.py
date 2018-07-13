#!/usr/bin/env python

import xmlrpclib
from SimpleXMLRPCServer import SimpleXMLRPCServer
from datetime import datetime

def getYear():
    t = datetime.now()
    YR = t.year
    print "Year is ",str(YR)
    return YR

def getMonth():
    t = datetime.now()
    MTH = t.month
    print "Month is ",MTH
    return MTH

def getDate():
    t = datetime.now()
    DTE = t.day
    print "Date is ",DTE
    return DTE

def getHour():
    t = datetime.now()
    HR = t.hour
    print "Hour is ",HR
    return HR

def getMinute():
    t = datetime.now()
    MIN = t.minute
    print "Minute is ",MIN
    return MIN

def getSecond():
    t = datetime.now()
    SEC = t.second
    print "Second is ",SEC
    return SEC

def getMillisecond():
    t = datetime.now()
    MS = t.microsecond
    MS = MS / 1000.0
    print "Microsecond is ",MS
    return MS

#getYear()
#getMonth()
#getDate()
#getHour()
#getMinute()
#getSecond()
#getMillisecond()

server = SimpleXMLRPCServer(("", 44044), allow_none=True)
print "Listening on port 44044..."
server.register_function(getYear, "getYear")
server.register_function(getMonth, "getMonth")
server.register_function(getDate, "getDate")
server.register_function(getHour, "getHour")
server.register_function(getMinute, "getMinute")
server.register_function(getSecond, "getSecond")
server.register_function(getMillisecond, "getMillisecond")
server.serve_forever()