import React, {useContext} from "react";
import {Outlet, Route, Routes, useNavigate} from "react-router-dom";
import {AmateurSoccerGroupMenu} from "./AmateurSoccerGroupMenu";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";
import {AmateurSoccerGroupsLoadContext, MenuContext} from "../App";
import {useSessionState} from "../../api/useSessionState";
import {PlayerList} from "../../components/amateurSoccerGroups/PlayerList";
import {PlayerNew} from "../../components/amateurSoccerGroups/PlayerNew";

export function AmateurSoccerGroupPage() {
    const navigate = useNavigate()
    const menu = useContext(MenuContext)
    const amateurSoccerGroupLoadContext = useContext(AmateurSoccerGroupsLoadContext)
    const [userDataCreationUrl] = useSessionState("amateurSoccerGroupUserDataCreationUrl") //TODO: weird! Should be replace if used GraphQL
    const [creationUrl, setCreationUrl] = useSessionState("amateurSoccerGroupPlayerCreationUrl")

    return <main>
        <AmateurSoccerGroupMenu menu={menu}/>
        <div className={"m-3"}>
            <Outlet/>
        </div>
        <Routes>
            <Route path="view" element={
                <AmateurSoccerGroupView
                    url={menu.amateurSoccerGroup.viewUrl.value}
                    setGamedaysUrl={menu.amateurSoccerGroup.gamedaysUrl.set}
                    setCalculateRankingUrl={menu.amateurSoccerGroup.calculateRankingUrl.set}
                    setPlayersUrl={menu.amateurSoccerGroup.playersUrl.set}
                    setGamedayCreationurl={menu.amateurSoccerGroup.gamedaysCreationUrl.set}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    creationUrl={menu.amateurSoccerGroup.creationUrl.value}
                    userDataCreationUrl={userDataCreationUrl}
                    setCreatedAmateurSoccerGroupUrl={(link) => {
                        menu.amateurSoccerGroup.viewUrl.set(link)
                        amateurSoccerGroupLoadContext.set(true)
                        navigate('/amateurSoccerGroups/view')
                    }}
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
