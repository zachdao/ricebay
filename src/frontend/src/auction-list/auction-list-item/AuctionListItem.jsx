import React from 'react';
import { Flex, Text, View } from '@adobe/react-spectrum';
import styled from 'styled-components';
import Image from '@spectrum-icons/workflow/Image';
import { useNavigate } from 'react-router-dom';

export const AuctionListItem = ({ auction }) => {
    const navigate = useNavigate();
    const truncate = (text, size) =>
        text.length > size ? text.substring(0, size) + '...' : text;
    return (
        <AuctionItemGrid onClick={() => navigate(`/auction/${auction.id}`)}>
            <View gridArea="image" width="100%" height="100%">
                {auction.images && auction.images.length ? (
                    <img src={auction.images[0]} alt="image of auction item" />
                ) : (
                    <View width="100%" height="100%" backgroundColor="gray-300">
                        <Image width="100%" height="100%" />
                    </View>
                )}
            </View>
            <Flex
                gridArea="content"
                justifyContent="start"
                alignItems="start"
                direction="column"
                margin="10px"
            >
                <LargerText>{`${truncate(auction.title, 20)} - $${(
                    auction.currentBid || auction.minimumBid
                ).toFixed(2)}`}</LargerText>
                <Text>{truncate(auction.description, 60)}</Text>
            </Flex>
        </AuctionItemGrid>
    );
};

const AuctionItemGrid = styled.div`
    grid-template-areas: 'image content';
    grid-template-columns: 1fr 3fr;
    display: grid;
    box-shadow: grey 2px 2px 3px 0px;
    border-radius: 3px;
    max-height: 86px;
    overflow: hidden;
    margin: 10px;
    cursor: pointer;
    height: 100%;
    width: 100%;
    &:hover {
        box-shadow: none;
        border: 1px solid grey;
    }
`;

const LargerText = styled.div`
    font-size: 110%;
    font-weight: lighter;
`;
