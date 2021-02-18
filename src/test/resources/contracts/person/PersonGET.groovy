package contracts.person

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url $(consumer('persons/NAME1'),
                producer("persons/NAME1"))
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body("""
                {
                    "name": "NAME1"
                }
        """)
    }
}

