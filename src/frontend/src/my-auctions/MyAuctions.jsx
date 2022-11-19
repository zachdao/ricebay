import React from 'react';
import {
    Flex,
    Heading,
    Text,
    TextField,
    TableView,
    TableHeader,
    Column,
    TableBody,
    Row,
    Cell,
} from '@adobe/react-spectrum';
import Filter from '@spectrum-icons/workflow/Filter';

export const MyAuctions = () => {
    const rows = [
        {
            id: 1,
            title: 'guitar1',
            start_date: '5/5/55',
            status: 'Ongoing',
            minimumBid: '2.0',
            currentBid: '50.0',
        },
        {
            id: 2,
            title: 'guitar2',
            start_date: '5/5/55',
            status: 'Stopped',
            minimumBid: '5.0',
            currentBid: '20.0',
        },
        {
            id: 3,
            title: 'guitar3',
            start_date: '5/5/55',
            status: 'Stopped',
            minimumBid: '12.0',
            currentBid: '1000.0',
        },
    ];
    const columns = [
        { name: 'Auction Title', uid: 'title' },
        { name: 'Auction Start Date', uid: 'start_date' },
        { name: 'Auction Status', uid: 'status' },
        { name: 'Bid Increment', uid: 'minimumBid' },
        { name: 'Current Bid', uid: 'currentBid' },
    ];

    // TODO Make sure dates and double type gets translated into table correctly
    // TODO Filter the list based on the keyword and auction titles
    // TODO Tie in with backend
    // TODO Sort list based on date

    // Get our list of owned auctions, where appResponse is AppResponse<List<Auction>>
    // const { sellerId } = useParams();
    // const { appResponse, error, status } = useHttpQuery(`/auctions/search`);
    // const auctions = appResponse?.data;

    // Display a toast if we have an error
    // useEffect(() => {
    //     if (
    //         error ||
    //         (status && status !== 200) ||
    //         (appResponse && !appResponse.success)
    //     ) {
    //         toast.custom((t) => (
    //             <Toast
    //                 message={
    //                     appResponse?.msg ||
    //                     'Failed to find owned auctions! Try again later'
    //                 }
    //                 type="negative"
    //                 dismissFn={() => toast.remove(t.id)}
    //             />
    //         ));
    //     }
    // }, [appResponse, error, status]);

    return (
        <Flex
            margin="auto"
            marginTop="size-400"
            height="80%"
            width="60%"
            direction="column"
            gap="size-200"
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
                <TableBody items={rows}>
                    {(item) => (
                        <Row>
                            {(columnKey) => <Cell>{item[columnKey]}</Cell>}
                        </Row>
                    )}
                </TableBody>
            </TableView>
        </Flex>
    );
};
