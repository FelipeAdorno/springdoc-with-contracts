package contracts.person

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url $(consumer('persons'),
                producer("persons"))
        headers {
            contentType(applicationJson())
        }
        body("""
        {
            "name": "Felipe"
        }  
        """)
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body("""
                {
                    "name": "Felipe"
                }
        """)
    }
}

