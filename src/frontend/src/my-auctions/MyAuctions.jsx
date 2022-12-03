import React, {useContext, useEffect} from 'react';
import {
    Flex,
    Heading,
    TextField,
    TableView,
    TableHeader,
    Column,
    TableBody,
    Row,
    Cell,
} from '@adobe/react-spectrum';
import Filter from '@spectrum-icons/workflow/Filter';
import { useHttpQuery } from "../http-query/use-http-query";
import { UserContext } from '../user.context';
import { useNavigate } from "react-router-dom";
import { Link } from '@adobe/react-spectrum';


export const MyAuctions = () => {
    const columns = [
        { name: 'Auction Title', uid: 'title' },
        { name: 'Auction Start Date', uid: 'startDate' },
        { name: 'Auction Status', uid: 'published' },
        { name: 'Bid Increment', uid: 'minimumBid' },
        { name: 'Current Bid', uid: 'currentBid' },
    ];

    // TODO Make sure dates and double type gets translated into table correctly
    // TODO Filter the list based on the keyword and auction titles
    // TODO Tie in with backend
    // TODO Sort list based on date
    const user = useContext(UserContext);

    // Get our list of owned auctions, where appResponse is AppResponse<List<Auction>>
    const {appResponse, error, status} = useHttpQuery(`/auctions/search`, {
        params: {
            sellerId: user?.id
        }
    });
    // const auctions = appResponse?.data;

    // Display a toast if we have an error
    useEffect(() => {
        if (
            error ||
            (status && status !== 200) ||
            (appResponse && !appResponse.success)
        ) {
            toast.custom((t) => (
                <Toast
                    message={
                        appResponse?.msg ||
                        'Failed to find owned auctions! Try again later'
                    }
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [appResponse, error, status]);

    const navigate = useNavigate();

    function mapItemValue(item, columnKey) {
        if (columnKey === 'published') {
            return item[columnKey] ? 'Published' : 'Not Published';
        } else if (columnKey === 'title') {
            return (<Link onPress={() => navigate("/auction/" + item.id)}>{item[columnKey]}</Link>);
        }
        return item[columnKey];
    }

    return (
        <Flex
            margin="auto"
            marginTop="size-400"
            height="80%"
            width="60%"
            direction="column"
            gap="size-200"
            data-testid="auctions-area"
        >
            <Heading level={1} alignSelf="center">
                Your Auctions
            </Heading>
            <Flex direction="row" gap="size-100" alignItems="end">
                <Filter aria-label="Filter" />
                <TextField label="Filter" />
            </Flex>
            <TableView>
                <TableHeader columns={columns}>
                    {(column) => (
                        <Column key={column.uid}>{column.name}</Column>
                    )}
                </TableHeader>
                <TableBody items={appResponse && appResponse.success ? appResponse.data : []}>
                    {(item) => (
                        <Row>
                            {(columnKey) => <Cell>{mapItemValue(item, columnKey)}</Cell>}
                        </Row>
                    )}
                </TableBody>
            </TableView>
        </Flex>
    );
};
