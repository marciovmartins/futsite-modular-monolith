import React, {useContext, useEffect, useState} from "react";
import {Outlet, Route, Routes, useLocation, useNavigate} from "react-router-dom";
import {AmateurSoccerGroupMenu} from "./AmateurSoccerGroupMenu";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";
import {MenuContext} from "../App";

export function AmateurSoccerGroupPage() {
    const location = useLocation()
    const navigate = useNavigate()
    const menu = useContext(MenuContext)
    const [userDataCreationUrl, setUserDataCreationUrl] = useState()

    return <main>
        {!location.pathname.includes('/amateurSoccerGroups/new') &&
            <AmateurSoccerGroupMenu menu={menu}/>}
        <Outlet/>
        <Routes>
            <Route index element={
                <ResetAmateurSoccerGroup
                    setCreationUrl={menu.amateurSoccerGroup.creationUrl.set}
                    setUserDataCreationUrl={setUserDataCreationUrl}
                    setViewUrl={menu.amateurSoccerGroup.viewUrl.set}
                    setGamedaysUrl={menu.amateurSoccerGroup.gamedaysUrl.set}
                    setCalculateRankingUrl={menu.amateurSoccerGroup.calculateRankingUrl.set}
                >
                    <AmateurSoccerGroupList
                        setViewUrl={(link) => {
                            menu.amateurSoccerGroup.viewUrl.set(link)
                            navigate("/amateurSoccerGroups/view")
                        }}
                        setCreationUrl={menu.amateurSoccerGroup.creationUrl.set}
                        setUserDataCreationUrl={setUserDataCreationUrl}
                        urlToNewAmateurSoccerGroup={menu.amateurSoccerGroup.creationUrl.value
                            && "/amateurSoccerGroups/new"}
                    />
                </ResetAmateurSoccerGroup>
            }/>

            <Route path="view" element={
                <AmateurSoccerGroupView
                    url={menu.amateurSoccerGroup.viewUrl.value}
                    setGamedaysUrl={menu.amateurSoccerGroup.gamedaysUrl.set}
                    setCalculateRankingUrl={menu.amateurSoccerGroup.calculateRankingUrl.set}
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

            <Route path="ranking" element={
                <CalculateRanking
                    uri={menu.amateurSoccerGroup.calculateRankingUrl.value}
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