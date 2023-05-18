import React, {useEffect, useState} from "react";
import {Link, Outlet, Route, Routes, useLocation} from "react-router-dom";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";

export function AmateurSoccerGroupPage() {
    const state = useLocation().state
    const [viewLink, setViewLink] = useState(state?.amateurSoccerGroupLink)
    const [creationLink, setCreationLink] = useState()
    const [gamedaysLink, setGamedaysLink] = useState()
    const [calculateRankingLink, setCalculateRankingLink] = useState()

    return <main>
        <nav>
            {creationLink && !viewLink &&
                <Link to="/amateurSoccerGroups/new"> | New</Link>}

            {viewLink &&
                <Link to={"/amateurSoccerGroups/view"} state={{viewLink}}> | Amateur Soccer Group</Link>}

            {gamedaysLink &&
                <Link to={"/gamedays"} state={{gamedaysLink}}> | Gamedays</Link>}

            {calculateRankingLink &&
                <Link to={"/amateurSoccerGroups/ranking"}> | Ranking</Link>}
        </nav>

        <Outlet/>
        <Routes>
            <Route index element={
                <ResetAmateurSoccerGroup
                    setCreationLink={setCreationLink}
                    setViewLink={setViewLink}
                    setGamedaysLink={setGamedaysLink}
                    setCalculateRankingLink={setCalculateRankingLink}
                >
                    <AmateurSoccerGroupList
                        setViewLink={setViewLink}
                        setCreationLink={setCreationLink}
                    />
                </ResetAmateurSoccerGroup>
            }/>

            <Route path="view" element={
                <AmateurSoccerGroupView
                    uri={viewLink}
                    setGamedaysLink={setGamedaysLink}
                    setCalculateRankingLink={setCalculateRankingLink}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    creationLink={creationLink}
                    setViewLink={setViewLink}
                />
            }/>

            <Route path="ranking" element={
                <CalculateRanking
                    uri={calculateRankingLink}
                />
            }/>
        </Routes>
    </main>;
}

function ResetAmateurSoccerGroup(props) {
    useEffect(() => {
        props.setCreationLink(undefined)
        props.setViewLink(undefined)
        props.setGamedaysLink(undefined)
        props.setCalculateRankingLink(undefined)
    }, [])
    return props.children;
}