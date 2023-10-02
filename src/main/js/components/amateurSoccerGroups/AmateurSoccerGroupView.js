import React, {useEffect, useState} from "react";
import {fetchGraphQL} from "../../api/fetchGraphQL";

export function AmateurSoccerGroupView(
    {id}
) {
    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        userData: {
            name: ''
        }
    })

    useEffect(() => {
        if (id === undefined) return;

        fetchGraphQL(`{
          amateurSoccerGroupById(id: "${id}") {
            userData {
              name
            }
          }
        }`)
            .then(response => response.data.amateurSoccerGroupById)
            .then(setAmateurSoccerGroup)
    }, [id])

    return (<div>
        <h1>{amateurSoccerGroup.userData.name}</h1>
    </div>)
}