import React, {useContext, useEffect, useState} from "react";
import {Outlet, Route, Routes, useNavigate} from "react-router-dom";
import {useSessionState} from "../../api/useSessionState";
import {fetchUrl} from "../../api/fetchUrl";
import {MenuContext} from "../App";
import {AmateurSoccerGroupMenu} from "./AmateurSoccerGroupMenu";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";
import {PlayerList} from "../../components/amateurSoccerGroups/PlayerList";
import {PlayerNew} from "../../components/amateurSoccerGroups/PlayerNew";
import {GamedayList} from "../../components/amateurSoccerGroups/GamedayList";
import {GamedayNew} from "../../components/amateurSoccerGroups/GamedayNew";

export function AmateurSoccerGroupPage() {
    const navigate = useNavigate()
    const menu = useContext(MenuContext)
    const [userDataCreationUrl] = useSessionState("amateurSoccerGroupUserDataCreationUrl") //TODO: weird! Should be replace if used GraphQL
    const [creationUrl, setCreationUrl] = useSessionState("amateurSoccerGroupPlayerCreationUrl")
    const [, setGamedayViewUrl] = useState()

    useEffect(() => {
        fetchUrl(menu.amateurSoccerGroup.viewUrl.value).then(amateurSoccerGroup => {
            menu.amateurSoccerGroup.gamedaysCreationUrl.set(amateurSoccerGroup._links?.["create-gameday"]?.href)
            menu.amateurSoccerGroup.gamedaysUrl.set(amateurSoccerGroup._links?.["get-gamedays"]?.href)
            menu.amateurSoccerGroup.calculateRankingUrl.set(amateurSoccerGroup._links?.["calculate-ranking"]?.href)
            menu.amateurSoccerGroup.playersUrl.set(amateurSoccerGroup._links?.["get-players"]?.href)
        })
    }, [menu.amateurSoccerGroup.viewUrl.value]);

    return <main>
        <AmateurSoccerGroupMenu menu={menu}/>
        <div className={"m-3"}>
            <Outlet/>
        </div>
        <Routes>
            <Route path="view" element={
                <AmateurSoccerGroupView
                    url={menu.amateurSoccerGroup.viewUrl.value}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    creationUrl={menu.amateurSoccerGroup.creationUrl.value}
                    userDataCreationUrl={userDataCreationUrl}
                    setCreatedAmateurSoccerGroupUrl={(link) => {
                        menu.amateurSoccerGroup.viewUrl.set(link)
                        navigate('/amateurSoccerGroups/view')
                    }}
                />
            }/>

            <Route path="gamedays" element={
                <GamedayList
                    url={menu.amateurSoccerGroup.gamedaysUrl.value}
                    creationRedirectWhenEmptyUrl={"/amateurSoccerGroups/gamedays/new"}
                />
            }/>

            <Route path="gamedays/new" element={
                <GamedayNew
                    creationUrl={menu.amateurSoccerGroup.gamedaysCreationUrl.value}
                    setViewUrl={(link) => {
                        setGamedayViewUrl(link)
                        navigate("/amateurSoccerGroups/gamedays")
                    }}
                    amateurSoccerGroupUrl={menu.amateurSoccerGroup.viewUrl.value}
                    playersCreationUrl={"/amateurSoccerGroups/players/new"}
                />
            }/>

            <Route path="ranking" element={
                <CalculateRanking
                    url={menu.amateurSoccerGroup.calculateRankingUrl.value}
                />
            }/>

            <Route path="players" element={
                <PlayerList
                    url={menu.amateurSoccerGroup.playersUrl.value}
                    setCreationUrl={setCreationUrl}
                    toPlayerNew={() => navigate("/amateurSoccerGroups/players/new")}
                />
            }/>

            <Route path="players/new" element={
                <PlayerNew
                    creationUrl={creationUrl}
                    toPlayerList={() => navigate("/amateurSoccerGroups/players")}
                />
            }/>
        </Routes>
    </main>;
}
