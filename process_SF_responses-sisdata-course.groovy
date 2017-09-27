
	import java.util.ArrayList
	ArrayList errorList = new ArrayList() 
	ArrayList successList = new ArrayList() 
	 
	full_payloadList = new ArrayList()
	
	println "value from flowvar === " + flowVars['sfprocessid']
	
	full_payloadList = flowVars['org_payload']
	_temp_payload = flowVars['_temp_payload']

	def finalPayload = [:]
	 
	int cnt=0 
	
	for ( sfdcupdate in payload )  
	{ 
	 	def objMapError = [:] 
	 	def objMapSuccess = [:] 
	 	
	 	//println "inside if......"+sfdcupdate.errors[0].message.toString() 
	 	//println "each payload data : " + sfdcupdate.payload.toString()
	 	
	 	def sfid = org.mule.util.StringUtils.mid(org.mule.util.StringUtils.substringAfter(sfdcupdate.payload.toString(), flowVars['sfprocessid']), 0,18)
	 	

	 	def item = _temp_payload.find { p -> p.Id == sfid }
	    println "SF id : - " + sfid + " -- item ID " + item.id +  " ============ " + item.toString()
	    
	    def temp_fullDataItem = full_payloadList.findAll { q -> q.Opportunity == item.Opportunity__c  }
		
		println "temp_fullDataItem = " + temp_fullDataItem.toString()
		
		if (temp_fullDataItem != null )
		{
		    def fullDataItem = temp_fullDataItem.find { r -> r.Bisk_Course_ID ==  item.Bisk_Course_ID__c }
			
			println "fullDataItem = " + fullDataItem.toString()
			
			if (fullDataItem != null )
			{
				println "fullDataItem -  > " + fullDataItem.toString()
				println "MSU ID : -> " + fullDataItem.msuid;
		
				if(!sfdcupdate.success) 
			 	{ 
			 		objMapError.put('dbid',fullDataItem.msuid)
			 		objMapError.put('sfid',sfid)
			 		objMapError.put('error',sfdcupdate.errors[0].message)
			 		
			 		errorList.add(objMapError) 
				}
				else
				{
					objMapSuccess.put('dbid',fullDataItem.msuid)
					objMapSuccess.put('sfid',sfid) 
					successList.add(objMapSuccess)
				}
			}
		}
	} 
	
	finalPayload.put('success',successList)
	finalPayload.put('failure',errorList)
		
	return finalPayload
