import React, {useState} from 'react'
import {v4 as uuidv4} from 'uuid';

export function AmateurSoccerGroupNew(
    {creationUrl, userDataCreationUrl, setCreatedAmateurSoccerGroupUrl}
) {
    const [formData, setFormData] = useState({
        name: ''
    })
    const handleChange = (event) => {
        const {name, value} = event.target
        setFormData((prevFormData) => ({...prevFormData, [name]: value}))
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        const amateurSoccerGroupId = uuidv4()
        Promise.all([
            submitAmateurSoccerGroup(creationUrl, amateurSoccerGroupId),
            submitAmateurSoccerGroupUserData(userDataCreationUrl, amateurSoccerGroupId, formData)
        ]).then(values => {
            const [amateurSoccerGroupResult] = values
            setCreatedAmateurSoccerGroupUrl(amateurSoccerGroupResult?._links?.self?.href)
        })
    }

    return <div>
        <h1 className={"mb-3"}>Create Amateur Soccer Group</h1>
        <form onSubmit={handleSubmit}>
            <div className={"row g-3 align-items-center"}>
                <div className={"col-auto mb-3"}>
                    <label htmlFor={"inputName"} className={"form-label"}>Name:</label>
                </div>
                <div className={"col-auto mb-3"}>
                    <input type={"text"}
                           className={"form-control"}
                           id={"inputName"}
                           name={"name"}
                           value={formData.name}
                           onChange={handleChange}
                    />
                </div>
            </div>
            <button type='submit' className={"btn btn-primary"}>Submit</button>
        </form>
    </div>
}

function submitAmateurSoccerGroup(link, amateurSoccerGroupId) {
    return fetch(link, {
        method: 'POST',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({amateurSoccerGroupId: amateurSoccerGroupId})
    })
        .then(response => response.json())
}

function submitAmateurSoccerGroupUserData(link, amateurSoccerGroupId, formData) {
    return fetch(link, {
        method: 'POST',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({...formData, amateurSoccerGroupId: amateurSoccerGroupId})
    })
        .then(response => response.json())
}