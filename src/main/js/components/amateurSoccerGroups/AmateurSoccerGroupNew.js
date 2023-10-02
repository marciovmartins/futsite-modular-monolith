import React, {useState} from 'react'
import {v4 as uuidv4} from 'uuid';
import {fetchGraphQL} from "../../api/fetchGraphQL";

export function AmateurSoccerGroupNew(
    {setCreatedAmateurSoccerGroupId}
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
        void submitAmateurSoccerGroup(amateurSoccerGroupId, formData.name)
        setTimeout(() => setCreatedAmateurSoccerGroupId(amateurSoccerGroupId), 200)
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

function submitAmateurSoccerGroup(amateurSoccerGroupId, name) {
    return fetchGraphQL(`
        mutation {
          createAmateurSoccerGroup(
            id: "${amateurSoccerGroupId}"
            name: "${name}"
          ) {
            id
          }
        }
    `)
}