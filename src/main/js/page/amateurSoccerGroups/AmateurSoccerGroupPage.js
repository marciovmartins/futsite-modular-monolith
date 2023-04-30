import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";

export function AmateurSoccerGroupPage() {
    const [viewLink, setViewLink] = useState()
    const [creationLink, setCreationLink] = useState()

    return <main>
        <nav>
            <Link to="/amateurSoccerGroups"
                  onClick={() => setViewLink(undefined)}
            >List</Link> |{" "}

            {creationLink &&
                <Link to="/amateurSoccerGroups/new">New</Link>}
        </nav>

        {!viewLink &&
            <AmateurSoccerGroupList
                callbackViewLink={setViewLink}
                callbackCreationLink={setCreationLink}
            />}
        {viewLink &&
            <AmateurSoccerGroupView
                uri={viewLink}
            />}
    </main>;
}