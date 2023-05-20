import React, {useEffect, useState} from "react";
import {Link, Outlet, Route, Routes, useLocation, useNavigate} from "react-router-dom";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";

export function AmateurSoccerGroupPage() {
    const state = useLocation().state
    const navigate = useNavigate()

    const [viewUrl, setViewUrl] = useState(state?.amateurSoccerGroupLink)
    const [creationUrl, setCreationUrl] = useState()
    const [userDataCreationUrl, setUserDataCreationUrl] = useState()
    const [gamedaysUrl, setGamedaysUrl] = useState()
    const [calculateRankingUrl, setCalculateRankingUrl] = useState()

    return <main>
        <nav>
            {creationUrl && !viewUrl &&
                <Link to="/amateurSoccerGroups/new"> | New</Link>}

            {viewUrl &&
                <Link to={"/amateurSoccerGroups/view"} state={{viewUrl: viewUrl}}> | Amateur Soccer Group</Link>}

            {gamedaysUrl &&
                <Link to={"/gamedays"} state={{gamedaysLink: gamedaysUrl}}> | Gamedays</Link>}

            {calculateRankingUrl &&
                <Link to={"/amateurSoccerGroups/ranking"}> | Ranking</Link>}
        </nav>

        <Outlet/>
        <Routes>
            <Route index element={
                <ResetAmateurSoccerGroup
                    setCreationUrl={setCreationUrl}
                    setUserDataCreationUrl={setUserDataCreationUrl}
                    setViewUrl={setViewUrl}
                    setGamedaysUrl={setGamedaysUrl}
                    setCalculateRankingUrl={setCalculateRankingUrl}
                >
                    <AmateurSoccerGroupList
                        setViewUrl={(link) => {
                            setViewUrl(link)
                            navigate("/amateurSoccerGroups/view")
                        }}
                        setCreationUrl={setCreationUrl}
                        setUserDataCreationUrl={setUserDataCreationUrl}
                    />
                </ResetAmateurSoccerGroup>
            }/>

            <Route path="view" element={
                <AmateurSoccerGroupView
                    url={viewUrl}
                    setGamedaysUrl={setGamedaysUrl}
                    setCalculateRankingUrl={setCalculateRankingUrl}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    creationUrl={creationUrl}
                    userDataCreationUrl={userDataCreationUrl}
                    setCreatedAmateurSoccerGroupUrl={(link) => {
                        setViewUrl(link)
                        navigate('/amateurSoccerGroups/view')
                    }}
                />
            }/>

            <Route path="ranking" element={
                <CalculateRanking
                    uri={calculateRankingUrl}
                />
            }/>
        </Routes>
    </main>;
}

function ResetAmateurSoccerGroup(props) {
    useEffect(() => {
        props.setCreationUrl(undefined)
        props.setUserDataCreationUrl(undefined)
        props.setViewUrl(undefined)
        props.setGamedaysUrl(undefined)
        props.setCalculateRankingUrl(undefined)
    }, [])
    return props.children;
}